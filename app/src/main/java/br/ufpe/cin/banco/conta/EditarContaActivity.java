package br.ufpe.cin.banco.conta;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

import br.ufpe.cin.banco.R;

//Ver anotações TODO no código
public class EditarContaActivity extends AppCompatActivity {

    public static final String KEY_NUMERO_CONTA = "numeroDaConta";
    ContaViewModel viewModel;
    private Conta contaAtual;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_adicionar_conta);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        viewModel = new ViewModelProvider(this).get(ContaViewModel.class);

        Button btnAtualizar = findViewById(R.id.btnAtualizar);
        Button btnRemover = findViewById(R.id.btnRemover);
        EditText campoNome = findViewById(R.id.nome);
        EditText campoNumero = findViewById(R.id.numero);
        EditText campoCPF = findViewById(R.id.cpf);
        EditText campoSaldo = findViewById(R.id.saldo);
        campoNumero.setEnabled(false);

        Intent i = getIntent();
        String numeroConta = i.getStringExtra(KEY_NUMERO_CONTA);
        
        if (numeroConta != null && !numeroConta.isEmpty()) {
            new Thread(() -> {
                contaAtual = viewModel.getRepository().buscarPeloNumero(numeroConta);
                runOnUiThread(() -> {
                    if (contaAtual != null) {
                        campoNome.setText(contaAtual.nomeCliente);
                        campoNumero.setText(contaAtual.numero);
                        campoCPF.setText(contaAtual.cpfCliente);
                        campoSaldo.setText(String.valueOf(contaAtual.saldo));
                    }
                });
            }).start();
        }

        btnAtualizar.setText("Editar");
        btnAtualizar.setOnClickListener(
                v -> {
                    String nomeCliente = campoNome.getText().toString().trim();
                    String cpfCliente = campoCPF.getText().toString().trim();
                    String saldoConta = campoSaldo.getText().toString().trim();
                    
                    if (nomeCliente.isEmpty()) {
                        Toast.makeText(this, "Nome do cliente é obrigatório!", Toast.LENGTH_SHORT).show();
                        campoNome.requestFocus();
                        return;
                    }
                    
                    if (nomeCliente.length() < 5) {
                        Toast.makeText(this, "Nome deve ter pelo menos 5 caracteres!", Toast.LENGTH_SHORT).show();
                        campoNome.requestFocus();
                        return;
                    }
                    
                    if (cpfCliente.isEmpty()) {
                        Toast.makeText(this, "CPF do cliente é obrigatório!", Toast.LENGTH_SHORT).show();
                        campoCPF.requestFocus();
                        return;
                    }
                    
                    if (saldoConta.isEmpty()) {
                        Toast.makeText(this, "Saldo é obrigatório!", Toast.LENGTH_SHORT).show();
                        campoSaldo.requestFocus();
                        return;
                    }
                    
                    double saldo;
                    try {
                        saldo = Double.parseDouble(saldoConta);
                    } catch (NumberFormatException e) {
                        Toast.makeText(this, "Saldo deve ser um número válido!", Toast.LENGTH_SHORT).show();
                        campoSaldo.requestFocus();
                        return;
                    }
                    
                    Conta contaAtualizada = new Conta(numeroConta, saldo, nomeCliente, cpfCliente);
                    viewModel.atualizar(contaAtualizada);
                    
                    Toast.makeText(this, "Conta atualizada com sucesso!", Toast.LENGTH_SHORT).show();
                    finish();
                }
        );

        btnRemover.setOnClickListener(v -> {
            if (contaAtual != null) {
                viewModel.remover(contaAtual);
                Toast.makeText(this, "Conta removida com sucesso!", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }
}