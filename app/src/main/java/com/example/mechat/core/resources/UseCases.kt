package com.example.smartjobreminder.core.resources

import kotlinx.coroutines.flow.Flow

interface UseCases<SuccessDataType,Params>{
    suspend fun invoke(params :Params):SuccessDataType
}

interface FlowUseCase<SuccessDataType,Params>{
    suspend fun invoke(params: Params):Flow<SuccessDataType>
}

object EmptyParams
