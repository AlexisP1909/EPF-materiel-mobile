# Country Search App

This is a Kotlin-based Android application that allows users to search for countries and view detailed information about them. The app uses a `SearchView` for input and displays results in a `RecyclerView`.

## Features

1. **Search Countries**: Users can search for countries by name, capital, and region. The search functionality is implemented using a `SearchView` that is expanded by default, allowing users to click anywhere on the `SearchView` to start typing.

2. **View Country Details**: When a country is selected from the search results, the app navigates to a detailed view where more information about the country is displayed. This includes the country's flag, which is loaded from a URL using the Glide library for image loading and caching.

3. **Favorite Countries**: Users can mark countries as favorites for easy access later. Favorite countries can be viewed even when the device is offline, thanks to the caching capabilities of the Glide library.

4. **QR Code Scanning**: The app includes a feature to scan QR codes.

## How to Use

1. **Search for Countries**: Simply click on the `SearchView` at the top of the screen and start typing the name, capital, or region of the country you're looking for. The search results will update in real-time as you type.

2. **View Country Details**: Click on a country in the search results to view more details about it. The detailed view includes the country's flag, which is loaded from a URL and cached for offline viewing.

3. **Mark a Country as Favorite**: In the detailed view of a country, click on the "Add to Favorites" button to mark the country as a favorite. If you're offline, you can still view your favorite countries.

4. **Scan a QR Code**: Click on the "Scan QR Code" button to open the QR code scanner.