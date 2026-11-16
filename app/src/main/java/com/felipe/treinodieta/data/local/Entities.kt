package com.felipe.treinodieta.data.local

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

enum class TipoExercicio { MUSCULACAO, AEROBICO }

enum class TipoRefeicao { CAFE, ALMOCO, LANCHE, JANTAR }

/*
 * Convenção de datas: campos "data" guardam epochDay (dias desde 1970,
 * LocalDate.toEpochDay()). Consultas por dia/semana viram comparações
 * numéricas simples, sem parse de strings.
 */

/** Catálogo de exercícios reutilizável (nome + grupo muscular + tipo). */
@Entity(tableName = "exercicios")
data class Exercicio(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val nome: String,
    val grupoMuscular: String,
    val tipo: TipoExercicio,
)

/** Uma ida à academia (ou sessão mista com cardio). */
@Entity(
    tableName = "sessoes_treino",
    indices = [Index("data")],
)
data class SessaoTreino(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val data: Long,
    val titulo: String? = null,
    val nota: String? = null,
)

/**
 * Lançamento dentro de uma sessão: série de musculação (reps + carga) ou
 * atividade aeróbica (duração + distância/nota opcionais). Tabela única com
 * campos nulos — opção mais simples para o MVP; o [Exercicio.tipo] define
 * quais campos fazem sentido. Alternativa futura: separar em duas tabelas.
 */
@Entity(
    tableName = "registros_exercicio",
    foreignKeys = [
        ForeignKey(
            entity = SessaoTreino::class,
            parentColumns = ["id"],
            childColumns = ["sessaoId"],
            onDelete = ForeignKey.CASCADE,
        ),
        ForeignKey(
            entity = Exercicio::class,
            parentColumns = ["id"],
            childColumns = ["exercicioId"],
            onDelete = ForeignKey.RESTRICT,
        ),
    ],
    indices = [Index("sessaoId"), Index("exercicioId")],
)
data class RegistroExercicio(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val sessaoId: Long,
    val exercicioId: Long,
    /** Ordem do exercício dentro da sessão. */
    val ordem: Int,
    // Campos de musculação
    val repeticoes: Int? = null,
    val cargaKg: Double? = null,
    // Campos de aeróbico
    val duracaoMin: Int? = null,
    val distanciaKm: Double? = null,
    /** Ex.: "intensidade forte", ritmo percebido. */
    val nota: String? = null,
)

/** Biblioteca de alimentos: valores nutricionais POR PORÇÃO. */
@Entity(tableName = "alimentos")
data class Alimento(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val nome: String,
    /** Descrição da porção, ex.: "1 fatia", "100 g". */
    val porcaoDescricao: String,
    val kcal: Double,
    val proteinaG: Double,
    val carboidratoG: Double,
    val gorduraG: Double,
)

/** Refeição de um dia (café, almoço, lanche, jantar). */
@Entity(
    tableName = "refeicoes",
    indices = [Index("data")],
)
data class Refeicao(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val data: Long,
    val tipo: TipoRefeicao,
)

/**
 * Item lançado numa refeição. Os valores nutricionais são SNAPSHOT (copiados
 * na hora do registro), então editar a biblioteca depois não altera o
 * histórico — decisão central do plano de dados.
 */
@Entity(
    tableName = "itens_refeicao",
    foreignKeys = [
        ForeignKey(
            entity = Refeicao::class,
            parentColumns = ["id"],
            childColumns = ["refeicaoId"],
            onDelete = ForeignKey.CASCADE,
        ),
        ForeignKey(
            entity = Alimento::class,
            parentColumns = ["id"],
            childColumns = ["alimentoId"],
            onDelete = ForeignKey.SET_NULL,
        ),
    ],
    indices = [Index("refeicaoId"), Index("alimentoId")],
)
data class ItemRefeicao(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val refeicaoId: Long,
    /** Referência opcional à biblioteca; nulo = item avulso. */
    val alimentoId: Long? = null,
    val nome: String,
    val quantidade: Double,
    val kcal: Double,
    val proteinaG: Double,
    val carboidratoG: Double,
    val gorduraG: Double,
)

/** Peso corporal (Fase 2+): base para o gráfico de evolução. */
@Entity(
    tableName = "peso_corporal",
    indices = [Index("data")],
)
data class RegistroPeso(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val data: Long,
    val pesoKg: Double,
)
