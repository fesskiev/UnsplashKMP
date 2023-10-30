package com.unsplash.shared.data.utils.download

import io.ktor.utils.io.errors.IOException

class PhotoDownloadException : IOException("Can't download photo")