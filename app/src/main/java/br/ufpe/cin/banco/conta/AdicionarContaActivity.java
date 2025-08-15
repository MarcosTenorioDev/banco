package br.ufpe.cin.banco.conta;

import android.os.Bundle;
import android.view.View;
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
public class AdicionarContaActivity extends AppCompatActivity {

    ContaViewModel viewModel;

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

        btnAtualizar.setText("Inserir");
        btnRemover.setVisibility(View.GONE);

        btnAtualizar.setOnClickListener(
                v -> {
                    String nomeCliente = campoNome.getText().toString();
                    String cpfCliente = campoCPF.getText().toString();
                    String numeroConta = campoNumero.getText().toString();
                    String saldoConta = campoSaldo.getText().toString();
                    
                    //Validações dos campos
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
                    
                    if (numeroConta.isEmpty()) {
                        Toast.makeText(this, "Número da conta é obrigatório!", Toast.LENGTH_SHORT).show();
                        campoNumero.requestFocus();
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

                    Conta c = new Conta(numeroConta, Double.valueOf(saldoConta), nomeCliente, cpfCliente);
                    viewModel.inserir(c);
                    Toast.makeText(this, "Conta criada com sucesso!", Toast.LENGTH_SHORT).show();
                    finish();
                }
        );

    }
}