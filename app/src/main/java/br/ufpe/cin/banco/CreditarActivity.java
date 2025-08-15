package br.ufpe.cin.banco;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

import br.ufpe.cin.banco.transacoes.Transacao;
import br.ufpe.cin.banco.transacoes.TransacaoViewModel;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

//Ver anotações TODO no código
public class CreditarActivity extends AppCompatActivity {
    BancoViewModel viewModel;
    TransacaoViewModel transacaoViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_operacoes);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        viewModel = new ViewModelProvider(this).get(BancoViewModel.class);
        transacaoViewModel = new ViewModelProvider(this).get(TransacaoViewModel.class);

        TextView tipoOperacao = findViewById(R.id.tipoOperacao);
        EditText numeroContaOrigem = findViewById(R.id.numeroContaOrigem);
        TextView labelContaDestino = findViewById(R.id.labelContaDestino);
        EditText numeroContaDestino = findViewById(R.id.numeroContaDestino);
        EditText valorOperacao = findViewById(R.id.valor);
        Button btnOperacao = findViewById(R.id.btnOperacao);
        labelContaDestino.setVisibility(View.GONE);
        numeroContaDestino.setVisibility(View.GONE);
        valorOperacao.setHint(valorOperacao.getHint() + " creditado");
        tipoOperacao.setText("CREDITAR");
        btnOperacao.setText("Creditar");

        btnOperacao.setOnClickListener(
                v -> {
                    String numOrigem = numeroContaOrigem.getText().toString().trim();
                    String valorTexto = valorOperacao.getText().toString().trim();
                    
                    if (numOrigem.isEmpty()) {
                        Toast.makeText(this, "Número da conta é obrigatório!", Toast.LENGTH_SHORT).show();
                        numeroContaOrigem.requestFocus();
                        return;
                    }
                    
                    if (valorTexto.isEmpty()) {
                        Toast.makeText(this, "Valor é obrigatório!", Toast.LENGTH_SHORT).show();
                        valorOperacao.requestFocus();
                        return;
                    }
                    
                    double valor;
                    try {
                        valor = Double.parseDouble(valorTexto);
                        if (valor <= 0) {
                            Toast.makeText(this, "Valor deve ser maior que zero!", Toast.LENGTH_SHORT).show();
                            valorOperacao.requestFocus();
                            return;
                        }
                    } catch (NumberFormatException e) {
                        Toast.makeText(this, "Valor deve ser um número válido!", Toast.LENGTH_SHORT).show();
                        valorOperacao.requestFocus();
                        return;
                    }
                    
                    viewModel.creditar(numOrigem, valor);

                    String dataAtual = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault()).format(new Date());
                    Transacao transacao = new Transacao(0, 'C', numOrigem, valor, dataAtual);
                    transacaoViewModel.inserir(transacao);
                    
                    Toast.makeText(this, "Crédito realizado com sucesso!", Toast.LENGTH_SHORT).show();
                    finish();
                }
        );
    }
}