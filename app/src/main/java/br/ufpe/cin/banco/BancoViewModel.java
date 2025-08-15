package br.ufpe.cin.banco;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import br.ufpe.cin.banco.conta.Conta;
import br.ufpe.cin.banco.conta.ContaRepository;
import br.ufpe.cin.banco.transacoes.TransacaoRepository;

import java.util.List;

//Ver anotações TODO no código
public class BancoViewModel extends AndroidViewModel {
    private ContaRepository contaRepository;
    private TransacaoRepository transacaoRepository;

    public BancoViewModel(@NonNull Application application) {
        super(application);
        this.contaRepository = new ContaRepository(BancoDB.getDB(application).contaDAO());
        this.transacaoRepository = new TransacaoRepository(BancoDB.getDB(application).transacaoDAO());
    }

    void transferir(String numeroContaOrigem, String numeroContaDestino, double valor) {
        new Thread(() -> {
            var contaOrigem = contaRepository.buscarPeloNumero(numeroContaOrigem);
            var contaDestino = contaRepository.buscarPeloNumero(numeroContaDestino);

            if (contaOrigem != null && contaDestino != null) {
                contaOrigem.saldo = contaOrigem.saldo - valor;
                contaDestino.saldo = contaDestino.saldo + valor;

                contaRepository.atualizar(contaOrigem);
                contaRepository.atualizar(contaDestino);
            }
        }).start();
    }

    void creditar(String numeroConta, double valor) {
        new Thread(() -> {
            var conta = contaRepository.buscarPeloNumero(numeroConta);

            if (conta != null) {
                conta.saldo = conta.saldo + valor;
                contaRepository.atualizar(conta);
            }
        }).start();
    }

    void debitar(String numeroConta, double valor) {
        new Thread(() -> {
            var conta = contaRepository.buscarPeloNumero(numeroConta);
            if (conta != null) {
                conta.saldo = conta.saldo - valor;
                contaRepository.atualizar(conta);
            }
        }).start();
    }

    void buscarContasPeloNome(String nomeCliente) {
        new Thread(() -> {
            var contas = contaRepository.buscarPeloNome(nomeCliente);
        }).start();
    }

    void buscarContasPeloCPF(String cpfCliente) {
        new Thread(() -> {
            var contas = contaRepository.buscarPeloCPF(cpfCliente);
        }).start();
    }

    void buscarContaPeloNumero(String numeroConta) {
        new Thread(() -> {
            var conta = contaRepository.buscarPeloNumero(numeroConta);
        }).start();
    }

    public ContaRepository getContaRepository() {
        return contaRepository;
    }

}
