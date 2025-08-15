package br.ufpe.cin.banco.transacoes;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

//Ver anotações TODO no código
@Dao
public interface TransacaoDAO {

    @Insert
    void adicionar(Transacao t);

    //não deve ser possível editar ou remover uma transação

    @Query("SELECT * FROM transacoes ORDER BY dataTransacao DESC")
    LiveData<List<Transacao>> transacoes();

    // Buscar transações pelo número da conta
    @Query("SELECT * FROM transacoes WHERE numeroConta = :numeroConta ORDER BY dataTransacao DESC")
    List<Transacao> buscarPeloNumeroConta(String numeroConta);

    // Buscar transações pela data
    @Query("SELECT * FROM transacoes WHERE dataTransacao = :data ORDER BY dataTransacao DESC")
    List<Transacao> buscarPelaData(String data);

    // Buscar transações pela data e tipo (crédito)
    @Query("SELECT * FROM transacoes WHERE dataTransacao = :data AND tipoTransacao = 'C' ORDER BY dataTransacao DESC")
    List<Transacao> buscarPelaDataECredito(String data);

    // Buscar transações pela data e tipo (débito)
    @Query("SELECT * FROM transacoes WHERE dataTransacao = :data AND tipoTransacao = 'D' ORDER BY dataTransacao DESC")
    List<Transacao> buscarPelaDataEDebito(String data);

    // Buscar transações pelo número da conta e tipo (crédito)
    @Query("SELECT * FROM transacoes WHERE numeroConta = :numeroConta AND tipoTransacao = 'C' ORDER BY dataTransacao DESC")
    List<Transacao> buscarPeloNumeroContaECredito(String numeroConta);

    // Buscar transações pelo número da conta e tipo (débito)
    @Query("SELECT * FROM transacoes WHERE numeroConta = :numeroConta AND tipoTransacao = 'D' ORDER BY dataTransacao DESC")
    List<Transacao> buscarPeloNumeroContaEDebito(String numeroConta);

}
