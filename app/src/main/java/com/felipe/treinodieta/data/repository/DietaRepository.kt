package com.felipe.treinodieta.data.repository

import com.felipe.treinodieta.data.local.Alimento
import com.felipe.treinodieta.data.local.AlimentoDao
import com.felipe.treinodieta.data.local.ItemRefeicao
import com.felipe.treinodieta.data.local.Refeicao
import com.felipe.treinodieta.data.local.RefeicaoDao
import com.felipe.treinodieta.data.local.TotaisDia
import kotlinx.coroutines.flow.Flow

/** Acesso aos dados de dieta. Sem interface no MVP (uma única implementação). */
class DietaRepository(
    private val refeicaoDao: RefeicaoDao,
    private val alimentoDao: AlimentoDao,
) {

    fun observarRefeicoesDoDia(dia: Long): Flow<List<Refeicao>> = refeicaoDao.observarDoDia(dia)

    fun observarItens(refeicaoId: Long): Flow<List<ItemRefeicao>> = refeicaoDao.observarItens(refeicaoId)

    fun observarTotaisDoDia(dia: Long): Flow<TotaisDia> = refeicaoDao.observarTotaisDoDia(dia)

    fun observarAlimentos(): Flow<List<Alimento>> = alimentoDao.observarTodos()

    fun buscarAlimentos(busca: String): Flow<List<Alimento>> = alimentoDao.buscar(busca)

    // Escritas
    suspend fun salvarRefeicao(refeicao: Refeicao): Long = refeicaoDao.salvar(refeicao)

    suspend fun excluirRefeicao(refeicao: Refeicao) = refeicaoDao.excluir(refeicao)

    suspend fun inserirItem(item: ItemRefeicao): Long = refeicaoDao.inserirItem(item)

    suspend fun atualizarItem(item: ItemRefeicao) = refeicaoDao.atualizarItem(item)

    suspend fun excluirItem(item: ItemRefeicao) = refeicaoDao.excluirItem(item)

    suspend fun salvarAlimento(alimento: Alimento): Long = alimentoDao.salvar(alimento)

    suspend fun excluirAlimento(alimento: Alimento) = alimentoDao.excluir(alimento)
}
