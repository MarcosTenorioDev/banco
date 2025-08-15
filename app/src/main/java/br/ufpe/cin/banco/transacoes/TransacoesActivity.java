package br.ufpe.cin.banco.transacoes;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
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

import br.ufpe.cin.banco.BancoViewModel;
import br.ufpe.cin.banco.R;

//Ver anotações TODO no código
public class TransacoesActivity extends AppCompatActivity {
    BancoViewModel bancoViewModel;
    TransacaoViewModel transacaoViewModel;
    TransacaoAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_transacoes);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        bancoViewModel = new ViewModelProvider(this).get(BancoViewModel.class);
        transacaoViewModel = new ViewModelProvider(this).get(TransacaoViewModel.class);
        EditText aPesquisar = findViewById(R.id.pesquisa);
        Button btnPesquisar = findViewById(R.id.btn_Pesquisar);
        RadioGroup tipoTransacao = findViewById(R.id.tipoTransacao);
        RadioGroup tipoPesquisa = findViewById(R.id.tipoPesquisa);
        RecyclerView rvResultado = findViewById(R.id.rvResultado);
        adapter = new TransacaoAdapter(getLayoutInflater());
        rvResultado.setLayoutManager(new LinearLayoutManager(this));
        rvResultado.setAdapter(adapter);

        transacaoViewModel.transacoes.observe(this, transacoes -> {
            if (transacoes != null) {
                adapter.submitList(transacoes);
            }
        });

        btnPesquisar.setOnClickListener(v -> {
            String oQueFoiDigitado = aPesquisar.getText().toString().trim();
            
            if (oQueFoiDigitado.isEmpty()) {
                Toast.makeText(this, "Digite algo para pesquisar", Toast.LENGTH_SHORT).show();
                return;
            }
            
            int tipoPesquisaId = tipoPesquisa.getCheckedRadioButtonId();
            int tipoTransacaoId = tipoTransacao.getCheckedRadioButtonId();
            
            if (tipoPesquisaId == R.id.pelaData) {
                if (tipoTransacaoId == R.id.peloTipoCredito) {
                    buscarPorDataECredito(oQueFoiDigitado);
                } else if (tipoTransacaoId == R.id.peloTipoDebito) {
                    buscarPorDataEDebito(oQueFoiDigitado);
                } else {
                    buscarPorData(oQueFoiDigitado);
                }
            } else if (tipoPesquisaId == R.id.peloNumeroConta) {
                if (tipoTransacaoId == R.id.peloTipoCredito) {
                    buscarPorNumeroContaECredito(oQueFoiDigitado);
                } else if (tipoTransacaoId == R.id.peloTipoDebito) {
                    buscarPorNumeroContaEDebito(oQueFoiDigitado);
                } else {
                    buscarPorNumeroConta(oQueFoiDigitado);
                }
            }
        });
    }
   
    private void buscarPorData(String data) {
        new Thread(() -> {
            List<Transacao> transacoes = transacaoViewModel.getRepository().buscarPelaData(data);
            runOnUiThread(() -> {
                if (transacoes.isEmpty()) {
                    Toast.makeText(this, "Nenhuma transação encontrada para a data: " + data, Toast.LENGTH_SHORT).show();
                }
                adapter.submitList(transacoes);
            });
        }).start();
    }
    
    private void buscarPorDataECredito(String data) {
        new Thread(() -> {
            List<Transacao> transacoes = transacaoViewModel.getRepository().buscarPelaDataECredito(data);
            runOnUiThread(() -> {
                if (transacoes.isEmpty()) {
                    Toast.makeText(this, "Nenhuma transação de crédito encontrada para a data: " + data, Toast.LENGTH_SHORT).show();
                }
                adapter.submitList(transacoes);
            });
        }).start();
    }
    
    private void buscarPorDataEDebito(String data) {
        new Thread(() -> {
            List<Transacao> transacoes = transacaoViewModel.getRepository().buscarPelaDataEDebito(data);
            runOnUiThread(() -> {
                if (transacoes.isEmpty()) {
                    Toast.makeText(this, "Nenhuma transação de débito encontrada para a data: " + data, Toast.LENGTH_SHORT).show();
                }
                adapter.submitList(transacoes);
            });
        }).start();
    }
    
    private void buscarPorNumeroConta(String numeroConta) {
        new Thread(() -> {
            List<Transacao> transacoes = transacaoViewModel.getRepository().buscarPeloNumeroConta(numeroConta);
            runOnUiThread(() -> {
                if (transacoes.isEmpty()) {
                    Toast.makeText(this, "Nenhuma transação encontrada para a conta: " + numeroConta, Toast.LENGTH_SHORT).show();
                }
                adapter.submitList(transacoes);
            });
        }).start();
    }
    
    private void buscarPorNumeroContaECredito(String numeroConta) {
        new Thread(() -> {
            List<Transacao> transacoes = transacaoViewModel.getRepository().buscarPeloNumeroContaECredito(numeroConta);
            runOnUiThread(() -> {
                if (transacoes.isEmpty()) {
                    Toast.makeText(this, "Nenhuma transação de crédito encontrada para a conta: " + numeroConta, Toast.LENGTH_SHORT).show();
                }
                adapter.submitList(transacoes);
            });
        }).start();
    }
    
    private void buscarPorNumeroContaEDebito(String numeroConta) {
        new Thread(() -> {
            List<Transacao> transacoes = transacaoViewModel.getRepository().buscarPeloNumeroContaEDebito(numeroConta);
            runOnUiThread(() -> {
                if (transacoes.isEmpty()) {
                    Toast.makeText(this, "Nenhuma transação de débito encontrada para a conta: " + numeroConta, Toast.LENGTH_SHORT).show();
                }
                adapter.submitList(transacoes);
            });
        }).start();
    }
}