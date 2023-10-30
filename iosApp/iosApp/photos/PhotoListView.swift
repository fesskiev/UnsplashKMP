import SwiftUI
import URLImage
import shared

struct PhotoListView: View {
    
    @StateObject var viewModel: PhotoListVM = PhotoListVM()
    
    private var gridItemLayout = [GridItem(.fixed(180)), GridItem(.fixed(180))]
    
    var body: some View {
        ZStack {
            NavigationView {
                ScrollView {
                    LazyVGrid(columns: gridItemLayout, spacing: 8) {
                        let photos = viewModel.state?.photos ?? []
                        ForEach(photos, id: \.id) { item in
                            NavigationLink(destination: PhotoDetailsView(photo: item)) {
                                GridItemView(item: item)
                                    .onAppear {
                                        viewModel.loadMorePhotos(photo: item)
                                    }
                            }
                        }
                        let loadMore = viewModel.state?.paging.loadMore ?? false
                        if loadMore {
                            LoadingView()
                        }
                    }
                    .navigationTitle("Photos")
                    .padding()
                }
                .onAppear {
                    viewModel.getFirstPhotosPage()
                }
            }
            let isLoading = viewModel.state?.isLoading ?? false
            if isLoading {
                LoadingView()
            }
        }
        .alert(item: $viewModel.alertModel) { alertModel in
            Alert(title: Text(alertModel.message))
        }
    }
}

struct GridItemView: View {
    let item: Photo
    
    var body: some View {
        VStack {
            let url = URL(string: item.urls.small)
            RemoteImage(url: url!)
            Text(item.user.name)
        }
    }
}

struct RemoteImage: View {
    let url: URL
    
    var body: some View {
        URLImage(url) { image in
            image
                .resizable()
                .aspectRatio(contentMode: .fit)
        }
        .scaledToFill()
        .frame(minWidth: 0, maxWidth: .infinity)
        .frame(height: 180)
        .clipShape(RoundedRectangle(cornerRadius: 12))
        .contentShape(Rectangle())
    }
}
