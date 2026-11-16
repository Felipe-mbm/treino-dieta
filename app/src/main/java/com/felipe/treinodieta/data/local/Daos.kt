package com.felipe.treinodieta.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

/** Totais de kcal e macros de um dia (resultado da query agregada de [RefeicaoDao]). */
data class TotaisDia(
    val kcal: Double,
    val proteinaG: Double,
    val carboidratoG: Double,
    val gorduraG: Double,
)

@Dao
interface ExercicioDao {

    @Query("SELECT * FROM exercicios ORDER BY nome")
    fun observarTodos(): Flow<List<Exercicio>>

    @Query("SELECT * FROM exercicios WHERE id = :id")
    suspend fun porId(id: Long): Exercicio?

    @Upsert
    suspend fun salvar(exercicio: Exercicio)

    @Delete
    suspend fun excluir(exercicio: Exercicio)
}

@Dao
interface SessaoDao {

    @Query("SELECT * FROM sessoes_treino ORDER BY data DESC, id DESC")
    fun observarTodas(): Flow<List<SessaoTreino>>

    @Query("SELECT * FROM sessoes_treino WHERE id = :id")
    fun observarPorId(id: Long): Flow<SessaoTreino?>

    @Upsert
    suspend fun salvar(sessao: SessaoTreino): Long

    @Delete
    suspend fun excluir(sessao: SessaoTreino)
}

@Dao
interface RegistroDao {

    @Query("SELECT * FROM registros_exercicio WHERE sessaoId = :sessaoId ORDER BY ordem, id")
    fun observarDaSessao(sessaoId: Long): Flow<List<RegistroExercicio>>

    /** Volume total (carga x reps) das sessões num intervalo de dias, inclusivo. */
    @Query(
        """
        SELECT COALESCE(SUM(r.cargaKg * r.repeticoes), 0)
        FROM registros_exercicio r
        INNER JOIN sessoes_treino s ON r.sessaoId = s.id
        WHERE s.data BETWEEN :inicio AND :fim
        """,
    )
    fun volumeTotal(inicio: Long, fim: Long): Flow<Double>

    @Upsert
    suspend fun salvar(registro: RegistroExercicio)

    @Delete
    suspend fun excluir(registro: RegistroExercicio)
}

@Dao
interface AlimentoDao {

    @Query("SELECT * FROM alimentos ORDER BY nome")
    fun observarTodos(): Flow<List<Alimento>>

    @Query("SELECT * FROM alimentos WHERE nome LIKE '%' || :busca || '%' ORDER BY nome")
    fun buscar(busca: String): Flow<List<Alimento>>

    @Upsert
    suspend fun salvar(alimento: Alimento): Long

    @Delete
    suspend fun excluir(alimento: Alimento)
}

@Dao
interface RefeicaoDao {

    @Query("SELECT * FROM refeicoes WHERE data = :dia ORDER BY tipo, id")
    fun observarDoDia(dia: Long): Flow<List<Refeicao>>

    @Query("SELECT * FROM itens_refeicao WHERE refeicaoId = :refeicaoId ORDER BY id")
    fun observarItens(refeicaoId: Long): Flow<List<ItemRefeicao>>

    /** Totais do dia somando todos os itens de todas as refeições. */
    @Query(
        """
        SELECT
            COALESCE(SUM(i.kcal), 0) AS kcal,
            COALESCE(SUM(i.proteinaG), 0) AS proteinaG,
            COALESCE(SUM(i.carboidratoG), 0) AS carboidratoG,
            COALESCE(SUM(i.gorduraG), 0) AS gorduraG
        FROM itens_refeicao i
        INNER JOIN refeicoes r ON i.refeicaoId = r.id
        WHERE r.data = :dia
        """,
    )
    fun observarTotaisDoDia(dia: Long): Flow<TotaisDia>

    @Upsert
    suspend fun salvar(refeicao: Refeicao): Long

    @Delete
    suspend fun excluir(refeicao: Refeicao)

    @Insert
    suspend fun inserirItem(item: ItemRefeicao): Long

    @Update
    suspend fun atualizarItem(item: ItemRefeicao)

    @Delete
    suspend fun excluirItem(item: ItemRefeicao)
}
