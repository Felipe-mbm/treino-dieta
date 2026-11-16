package com.felipe.treinodieta.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [
        Exercicio::class,
        SessaoTreino::class,
        RegistroExercicio::class,
        Alimento::class,
        Refeicao::class,
        ItemRefeicao::class,
        RegistroPeso::class,
    ],
    version = 1,
    exportSchema = false,
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun exercicioDao(): ExercicioDao
    abstract fun sessaoDao(): SessaoDao
    abstract fun registroDao(): RegistroDao
    abstract fun alimentoDao(): AlimentoDao
    abstract fun refeicaoDao(): RefeicaoDao

    companion object {
        fun criar(context: Context): AppDatabase =
            Room.databaseBuilder(context, AppDatabase::class.java, "treino-dieta.db")
                .build()
        // Ao mudar o schema nas próximas fases, adicionar Migration em vez
        // de fallbackToDestructiveMigration (não perder os dados reais).
    }
}
