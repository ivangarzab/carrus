//
//  ContentView.swift
//  carrus
//
//  Created by Ivan Garza Bermea on 1/1/24.
//

import SwiftUI
import shared

struct ContentView: View {
    var greet = Greeting().greet()
    var body: some View {
        VStack {
            Image(systemName: "globe")
                .imageScale(.large)
                .foregroundStyle(.tint)
            Text(greet)
        }
        .padding()
    }
}

#Preview {
    ContentView()
}
