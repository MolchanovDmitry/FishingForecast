package dmitry.molchanov.domain.usecase

import dmitry.molchanov.domain.model.Result
import dmitry.molchanov.domain.repository.ResultDataRepository
import kotlinx.coroutines.flow.Flow

class GetResultsUseCase(private val resultDataRepository: ResultDataRepository) {

    fun executeFlow(): Flow<List<Result>> = resultDataRepository.getResultsFlow()
}
