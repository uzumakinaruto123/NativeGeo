# NativeGeo


Cordova plugin to get current loction using Native Location Manager.

<br>
##### Platforms

- Android

<br>

##### Installation

```
cordova plugin add https://github.com/uzumakinaruto123/NativeGeo.git
```


##### Usage

```
declare var NativeGeo:any;

...

NativeGeo.getCurrentLocation((res)=>{
	console.log(res.latitude+', '+res.longitude);
},(err)=>{
	console.log('Error getting location', err);
});
```


##### Note: 
Application should have Location permissions to get current location