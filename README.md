# Blink - Hyper-Fast File Sharing
![blink](https://user-images.githubusercontent.com/40487883/66357241-02b42b80-e923-11e9-9829-61e258f2f18a.png)
This product is built at [AT&T 5G Hackathon](https://devpost.com/software/blink-hyperfast-file-sharing-qudz4c) 


## Inspiration
**New super-speed media transfer service between mobile devices over 5G network**

Due to the highly developed devices and sensors (high-resolution camera, 4k videos), people have much more opportunity to have GBs’ media. However, it is hard to share those contents with other people. Sending an email and using messaging platforms have file size limits and low speed. Using a cloud service is cumbersome because we want to share just one file instantly.

Our solution, powered by 5G technology, GPS location, and gRPC streaming connection, we suggest a new media transfer service. We build the fastest supercar!

## Our Vision
**Send any file in the blink of an eye**
- Deliver big files wherever you are by using gRPC connection on 5G Network
- Support seamless and continuous scan for candidates who want to receive the file based on location (GPS)

## Final Outputs
- Built _full-functioning prototype_ native application with gRPC and 5G Network
- Built a _continuous service discovery system_ based on GPS to make IPv6-alike architecture with IPv4
- **Filespot** feature: You can place your file on the map and everyone can access it if in a location nearby

...which means that:

- **Sending and receiving files with hyperfast network speed without any prior information about each other**

## How we built it
- Implemented native Android frontend using Kotlin, and Java
- Implemented fault-tolerant service discovery system using Go, gRPC over HTTP 2.0
- Implemented SQLite DB to store each users' metadata (Realtime GPS data, Filespots).

## Challenges we ran into
- Fight relatively slow uploading speed compare to download speed using background uploading.
- Streaming big files using RPC protocol
- Slicing file into chunk of bytes before uploading to accelerate speed and to load in small memory

## What's next?
After IPv6 becomes ordinary, so edge device has its own IP, our service discovery server will be the **service discovery server**, which means what the server does is only discovery, **not even touch the file.** Our server will open a temporary web server while sharing the file. By super-speed downloading speed, sharing the file in our normal life will be more convenient and easy.

## Accomplishments & What we learned
We solved the traditional server overload problem that it had to be store all of the files that users want to send, by using 5G technology and P2P connection. Everything we handled was first time to us; Android native programming, server programming combining gRPC and Go, etc. But we did it! It was an amazing experience.

We also learned how to handle network protocols and 5G. It was nice experience to try state of the art network technology :)
