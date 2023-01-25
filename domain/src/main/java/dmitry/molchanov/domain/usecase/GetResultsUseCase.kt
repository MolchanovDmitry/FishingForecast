package dmitry.molchanov.domain.usecase

import dmitry.molchanov.domain.ioDispatcher
import dmitry.molchanov.domain.model.Result
import dmitry.molchanov.domain.repository.ResultDataRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn

class GetResultsUseCase(private val resultDataRepository: ResultDataRepository) {

    fun executeFlow(): Flow<List<Result>> =
        resultDataRepository.getResultsFlow().flowOn(ioDispatcher)
}
