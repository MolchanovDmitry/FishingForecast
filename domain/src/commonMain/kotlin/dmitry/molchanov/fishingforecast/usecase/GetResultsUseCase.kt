package dmitry.molchanov.fishingforecast.usecase

import dmitry.molchanov.fishingforecast.model.Result
import dmitry.molchanov.fishingforecast.repository.ResultDataRepository
import dmitry.molchanov.fishingforecast.utils.ioDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn

class GetResultsUseCase(private val resultDataRepository: ResultDataRepository) {

    fun executeFlow(): Flow<List<Result>> =
        resultDataRepository.getResultsFlow().flowOn(ioDispatcher)
}