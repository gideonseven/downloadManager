# Download Manager
> In this project, i'm showing how we can use [DownloadManager API] from Android Using Coroutine

## Pros / Cons
> Pros
- can easily use (no other dependency needed)
- work well on Marshmellow above (tested until Snow Cone - API 32)

> Cons
> - For LOLLIPOP
- We need to Set Write Permission Storage in Manifest, but luckily we can optimize the permission for LOLLIPOP only by using
```sh
<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" android:maxSdkVersion="21"/>
```
- if we not add this permission app will be crash on LOLLIPOP device only


## PDF URL
```sh
https://unec.edu.az/application/uploads/2014/12/pdf-sample.pdf

"pdf-sample.pdf" Splitted into 2 String
   const val PDF_NAME = "pdf-sample"
   const val PDF_EXTENSION = ".pdf"
```

## Gif Demo
<img src="https://user-images.githubusercontent.com/13301518/189175054-43c0e8b6-e075-46f9-93ea-eb0ba7637ff6.gif" width="250"/>



## Dependencies
| implementation | version |
| ------ | ------ |
| Timber |5.0.1 |
| lifecycle-runtime-ktx |2.5.1|
| mhiew:android-pdf-viewer |3.2.0-beta.1|
| lottie|4.1.0|


## ✨Happy coding ✨
[DownloadManager API]: <https://developer.android.com/reference/android/app/DownloadManager>