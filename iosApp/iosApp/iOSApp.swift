import SwiftUI
import shared

@main
struct iOSApp: App {
    
    init() {
        Koin.start()
    }
    
    var body: some Scene {
        WindowGroup {
            PhotoListView()
        }
    }
}
