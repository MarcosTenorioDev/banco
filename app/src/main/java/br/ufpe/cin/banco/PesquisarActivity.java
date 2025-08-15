package br.ufpe.cin.banco;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import br.ufpe.cin.banco.conta.Conta;
import br.ufpe.cin.banco.conta.ContaAdapter;

//Ver anotações TODO no código
public class PesquisarActivity extends AppCompatActivity {
    BancoViewModel viewModel;
    ContaAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_pesquisar);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        viewModel = new ViewModelProvider(this).get(BancoViewModel.class);
        EditText aPesquisar = findViewById(R.id.pesquisa);
        Button btnPesquisar = findViewById(R.id.btn_Pesquisar);
        RadioGroup tipoPesquisa = findViewById(R.id.tipoPesquisa);
        RecyclerView rvResultado = findViewById(R.id.rvResultado);
        adapter = new ContaAdapter(getLayoutInflater());
        rvResultado.setLayoutManager(new LinearLayoutManager(this));
        rvResultado.setAdapter(adapter);

        btnPesquisar.setOnClickListener(
                v -> {
                    String oQueFoiDigitado = aPesquisar.getText().toString().trim();
                    
                    if (oQueFoiDigitado.isEmpty()) {
                        Toast.makeText(this, "Digite algo para pesquisar", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    
                    int selectedId = tipoPesquisa.getCheckedRadioButtonId();
                    
                    if (selectedId == R.id.peloNomeCliente) {
                        buscarPorNome(oQueFoiDigitado);
                    } else if (selectedId == R.id.peloCPFcliente) {
                        buscarPorCPF(oQueFoiDigitado);
                    } else if (selectedId == R.id.peloNumeroConta) {
                        buscarPorNumeroConta(oQueFoiDigitado);
                    }
                }
        );

        adapter.submitList(new ArrayList<>());
    }
    
    private void buscarPorNome(String nome) {
        new Thread(() -> {
            List<Conta> contas = viewModel.getContaRepository().buscarPeloNome(nome);
            runOnUiThread(() -> {
                if (contas.isEmpty()) {
                    Toast.makeText(this, "Nenhuma conta encontrada para o nome: " + nome, Toast.LENGTH_SHORT).show();
                }
                adapter.submitList(contas);
            });
        }).start();
    }
    
    private void buscarPorCPF(String cpf) {
        new Thread(() -> {
            List<Conta> contas = viewModel.getContaRepository().buscarPeloCPF(cpf);
            runOnUiThread(() -> {
                if (contas.isEmpty()) {
                    Toast.makeText(this, "Nenhuma conta encontrada para o CPF: " + cpf, Toast.LENGTH_SHORT).show();
                }
                adapter.submitList(contas);
            });
        }).start();
    }
    
    private void buscarPorNumeroConta(String numeroConta) {
        new Thread(() -> {
            Conta conta = viewModel.getContaRepository().buscarPeloNumero(numeroConta);
            runOnUiThread(() -> {
                List<Conta> contas = new ArrayList<>();
                if (conta != null) {
                    contas.add(conta);
                } else {
                    Toast.makeText(this, "Conta não encontrada: " + numeroConta, Toast.LENGTH_SHORT).show();
                }
                adapter.submitList(contas);
            });
        }).start();
    }
}