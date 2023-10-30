import shared

class PhotoListVM: ObservableObject {
    
    private let viewModel: PhotoListViewModel = Koin.instance.get()
    
    @Published
    var state: PhotoListContract.State?
    @Published var alertModel: AlertModel?
    
    
    init()  {
        viewModel.viewState.collect(collector: FlowCollector<PhotoListContract.State>() { value in
            print(value)
            self.state = value
            if value.responseError != nil {
                self.alertModel = AlertModel(message: value.responseError?.message ?? "")
            }
                
        }) { (error) in
            print(error?.localizedDescription ?? "")
            self.alertModel = AlertModel(message: error?.localizedDescription ?? "")
        }
    }
    
    func getFirstPhotosPage() {
        viewModel.sendViewEvent(event: PhotoListContract.EventGetFirstPhotosPageAction())
        print("GET FIRST PHOTO PAGE")
    }
    
    func loadMorePhotos(photo: Photo) {
        if (photo.id == state?.photos.last?.id) {
            print("LOAD MORE PHOTOS")
            viewModel.sendViewEvent(event: PhotoListContract.EventLoadMorePhotosAction())
        }
    }
}
