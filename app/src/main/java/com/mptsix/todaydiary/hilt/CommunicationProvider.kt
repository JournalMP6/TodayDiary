package com.mptsix.todaydiary.hilt

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.mptsix.todaydiary.model.ServerRepository
import com.mptsix.todaydiary.model.ServerRepositoryHelper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class CommunicationProvider {
    @Singleton
    @Provides
    fun getObjectMapper(): ObjectMapper {
        return jacksonObjectMapper()
    }

    @Singleton
    @Provides
    fun getServerRepositoryHelper(objectMapper: ObjectMapper): ServerRepositoryHelper {
        return ServerRepositoryHelper(objectMapper)
    }

    @Singleton
    @Provides
    fun getServerRepository(serverRepositoryHelper: ServerRepositoryHelper): ServerRepository {
        return ServerRepository(serverRepositoryHelper)
    }
}