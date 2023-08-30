package rs.raf.rmaprojekat.data.datasources.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import rs.raf.rmaprojekat.data.models.area.AreaEntity
import rs.raf.rmaprojekat.data.models.category.CategoryEntity
import rs.raf.rmaprojekat.data.models.ingredients.IngredientEntity
import rs.raf.rmaprojekat.data.models.meal.MealEntity
import rs.raf.rmaprojekat.data.models.saved.SavedMealEntity
import rs.raf.rmaprojekat.data.models.user.UserEntity
import rs.raf.rmaprojekat.data.datasources.local.converters.StringListConverter


@Database(
    entities = [CategoryEntity::class, MealEntity::class, AreaEntity::class,
                IngredientEntity::class, SavedMealEntity::class, UserEntity::class],
    version = 18,
    exportSchema = false)
@TypeConverters(StringListConverter::class)
abstract class MainDataBase : RoomDatabase() {
    abstract fun getCategoryDao(): CategoryDao
    abstract fun getMealDao(): MealDao
    abstract fun getAreaDao(): AreaDao
    abstract fun getIngredientsDao(): IngredientsDao
    abstract fun getSavedMealDao(): SavedMealDao
    abstract fun getUserDao(): UserDao
}