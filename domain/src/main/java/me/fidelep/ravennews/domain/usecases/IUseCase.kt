package me.fidelep.ravennews.domain.usecases

interface IUseCase<I, O> {
    suspend operator fun invoke(param: I): O
}
