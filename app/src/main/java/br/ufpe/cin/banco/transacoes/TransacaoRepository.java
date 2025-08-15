package br.ufpe.cin.banco.transacoes;

import androidx.annotation.WorkerThread;
import androidx.lifecycle.LiveData;

import java.util.List;

//Ver anotações TODO no código
public class TransacaoRepository {
    private TransacaoDAO dao;
    private LiveData<List<Transacao>> transacoes;

    public TransacaoRepository(TransacaoDAO dao) {
        this.dao = dao;
        this.transacoes = dao.transacoes();
    }

    public LiveData<List<Transacao>> getTransacoes() {
        return this.transacoes;
    }

    @WorkerThread
    public void inserir(Transacao t) {
        dao.adicionar(t);
    }

    // Buscar transações pelo número da conta
    @WorkerThread
    public List<Transacao> buscarPeloNumeroConta(String numeroConta) {
        return dao.buscarPeloNumeroConta(numeroConta);
    }

    // Buscar transações pela data
    @WorkerThread
    public List<Transacao> buscarPelaData(String data) {
        return dao.buscarPelaData(data);
    }

    // Buscar transações pela data e tipo (crédito)
    @WorkerThread
    public List<Transacao> buscarPelaDataECredito(String data) {
        return dao.buscarPelaDataECredito(data);
    }

    // Buscar transações pela data e tipo (débito)
    @WorkerThread
    public List<Transacao> buscarPelaDataEDebito(String data) {
        return dao.buscarPelaDataEDebito(data);
    }

    // Buscar transações pelo número da conta e tipo (crédito)
    @WorkerThread
    public List<Transacao> buscarPeloNumeroContaECredito(String numeroConta) {
        return dao.buscarPeloNumeroContaECredito(numeroConta);
    }

    // Buscar transações pelo número da conta e tipo (débito)
    @WorkerThread
    public List<Transacao> buscarPeloNumeroContaEDebito(String numeroConta) {
        return dao.buscarPeloNumeroContaEDebito(numeroConta);
    }
}
