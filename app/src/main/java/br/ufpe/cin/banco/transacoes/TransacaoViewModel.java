package br.ufpe.cin.banco.transacoes;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.annotation.WorkerThread;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;

import br.ufpe.cin.banco.BancoDB;
import br.ufpe.cin.banco.conta.Conta;

//Ver anotações TODO no código
public class TransacaoViewModel extends AndroidViewModel {

    private TransacaoRepository repository;
    public LiveData<List<Transacao>> transacoes;

    public TransacaoViewModel(@NonNull Application application) {
        super(application);
        this.repository = new TransacaoRepository(BancoDB.getDB(application).transacaoDAO());
        this.transacoes = repository.getTransacoes();
    }

    public void inserir(Transacao t) {
        new Thread(() -> repository.inserir(t)).start();
    }

    // Buscar transações pelo número da conta
    void buscarTransacoesPeloNumeroConta(String numeroConta) {
        new Thread(() -> repository.buscarPeloNumeroConta(numeroConta)).start();
    }

    // Buscar transações pela data
    void buscarTransacoesPelaData(String data) {
        new Thread(() -> repository.buscarPelaData(data)).start();
    }

    // Buscar transações pela data e tipo (crédito)
    void buscarTransacoesPelaDataECredito(String data) {
        new Thread(() -> repository.buscarPelaDataECredito(data)).start();
    }

    // Buscar transações pela data e tipo (débito)
    void buscarTransacoesPelaDataEDebito(String data) {
        new Thread(() -> repository.buscarPelaDataEDebito(data)).start();
    }

    // Buscar transações pelo número da conta e tipo (crédito)
    void buscarTransacoesPeloNumeroContaECredito(String numeroConta) {
        new Thread(() -> repository.buscarPeloNumeroContaECredito(numeroConta)).start();
    }

    // Buscar transações pelo número da conta e tipo (débito)
    void buscarTransacoesPeloNumeroContaEDebito(String numeroConta) {
        new Thread(() -> repository.buscarPeloNumeroContaEDebito(numeroConta)).start();
    }

    public TransacaoRepository getRepository() {
        return repository;
    }
}
