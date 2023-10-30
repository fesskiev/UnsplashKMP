import shared
import SwiftUI

struct PhotoDetailsView: View {
    
    @StateObject var viewModel: PhotoDetailsVM = PhotoDetailsVM()
    
    var photo: Photo
    
    var body: some View {
        let url = URL(string: photo.urls.small)
        RemoteImage(url: url!)
    }
}

struct PhotoDetailsView_Previews: PreviewProvider {
  static var previews: some View {
      PhotoDetailsView(photo: FakeDataSourceKt.generateFakePhoto())
  }
}
