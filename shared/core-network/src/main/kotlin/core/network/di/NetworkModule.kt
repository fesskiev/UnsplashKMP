package core.network.di

import core.network.client.provideKtorClient
import core.network.photos.PhotosNetworkSource
import core.network.photos.PhotosNetworkSourceImpl
import org.koin.dsl.module

val coreNetworkModule = module {
    single { provideKtorClient() }
    single<PhotosNetworkSource> { PhotosNetworkSourceImpl(get()) }
}
