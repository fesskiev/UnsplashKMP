import shared

class PhotoDetailsVM : ObservableObject {
    
    private let viewModel: PhotoDetailsViewModel = Koin.instance.get()
    
    @Published
    var state: PhotoDetailsContract.State?
    @Published var alertModel: AlertModel?
    
}
