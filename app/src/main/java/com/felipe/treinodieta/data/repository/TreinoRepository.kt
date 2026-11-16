package com.felipe.treinodieta.data.repository

import com.felipe.treinodieta.data.local.Exercicio
import com.felipe.treinodieta.data.local.ExercicioDao
import com.felipe.treinodieta.data.local.RegistroDao
import com.felipe.treinodieta.data.local.RegistroExercicio
import com.felipe.treinodieta.data.local.SessaoDao
import com.felipe.treinodieta.data.local.SessaoTreino
import kotlinx.coroutines.flow.Flow

/** Acesso aos dados de treino. Sem interface no MVP (uma única implementação). */
class TreinoRepository(
    private val sessaoDao: SessaoDao,
    private val exercicioDao: ExercicioDao,
    private val registroDao: RegistroDao,
) {

    // Consultas reativas: a UI se atualiza sozinha quando o banco muda
    fun observarSessoes(): Flow<List<SessaoTreino>> = sessaoDao.observarTodas()

    fun observarSessao(id: Long): Flow<SessaoTreino?> = sessaoDao.observarPorId(id)

    fun observarExercicios(): Flow<List<Exercicio>> = exercicioDao.observarTodos()

    fun observarRegistrosDaSessao(sessaoId: Long): Flow<List<RegistroExercicio>> =
        registroDao.observarDaSessao(sessaoId)

    fun volumeTotal(inicio: Long, fim: Long): Flow<Double> = registroDao.volumeTotal(inicio, fim)

    suspend fun exercicioPorId(id: Long): Exercicio? = exercicioDao.porId(id)

    // Escritas
    suspend fun salvarSessao(sessao: SessaoTreino): Long = sessaoDao.salvar(sessao)

    suspend fun excluirSessao(sessao: SessaoTreino) = sessaoDao.excluir(sessao)

    suspend fun salvarExercicio(exercicio: Exercicio) = exercicioDao.salvar(exercicio)

    suspend fun excluirExercicio(exercicio: Exercicio) = exercicioDao.excluir(exercicio)

    suspend fun salvarRegistro(registro: RegistroExercicio) = registroDao.salvar(registro)

    suspend fun excluirRegistro(registro: RegistroExercicio) = registroDao.excluir(registro)
}
