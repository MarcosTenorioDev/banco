package br.ufpe.cin.banco.conta;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

//Ver anotações TODO no código
@Dao
public interface ContaDAO {

    @Insert(entity = Conta.class, onConflict = OnConflictStrategy.REPLACE)
    void adicionar(Conta c);

    @androidx.room.Delete
    void remover(Conta c);

    @androidx.room.Update
    void atualizar(Conta c);

    @Query("SELECT * FROM contas ORDER BY numero ASC")
    LiveData<List<Conta>> contas();

    @Query("SELECT * FROM contas ORDER BY numero ASC")
    List<Conta> todasContas();

    @Query("SELECT * FROM contas WHERE numero = :numeroConta")
    Conta buscarPeloNumero(String numeroConta);

    @Query("SELECT * FROM contas WHERE nomeCliente LIKE '%' || :nomeCliente || '%' ORDER BY nomeCliente ASC")
    List<Conta> buscarPeloNome(String nomeCliente);

    @Query("SELECT * FROM contas WHERE cpfCliente = :cpfCliente ORDER BY numero ASC")
    List<Conta> buscarPeloCPF(String cpfCliente);

}
