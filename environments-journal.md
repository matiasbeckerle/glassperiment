---
layout: page
title: Environment's Journal
---

### 1) A little research

We aimed to make an application for Glass. The research began and we found that the documentation is very limited. And there *isn't* an *emulator*. Code examples? Just the three apps from Google and a few posts of some doubtful hello world's...

*Ok, now what?*

Luckily, the company has a Glass to use and test, so we had to make it work.

### 2) Setting the environment (in Windows 8)

#### Eclipse and GDK

The first thing we did was getting an Android environment fully functional: an Eclipse ready to run an Android hello world app. Then we have to download (with *Android SDK Manager*) the **Glass Development Kit Sneak Peek** for *API 15*.

#### Connecting Glass

1. On Glass, go to **Settings > Device Info > Turn on debug**.
2. Connect Glass to your computer.
3. Pray.
4. On Eclipse, go to **Window > Open Perspective > DDMS** and verify that Glass appears in the Devices tab.

Yup. We know. The screen is empty. Nothing seems to work. Those ones are the guidelines from official documentation. Don't worry. We were able to find out why.

##### The real steps

First of all, we want to send a big *thank you* to some users @ stackoverflow that helped us a lot. They are [matthew06854](http://stackoverflow.com/users/3046253/matthew06854) and [avalanchis](http://stackoverflow.com/users/157845/avalanchis). The related topics:

* [ddms unable to see google glass](http://stackoverflow.com/questions/20390624/ddms-unable-to-see-google-glass)
* [google glass isnt showing up as a device on eclipse under ddms help please](http://stackoverflow.com/questions/20556137/google-glass-isnt-showing-up-as-a-device-on-eclipse-under-ddms-help-please)

1. Apparently Windows won't install the driver if you don't disable the **driver signature enforcement**. This is a step that you have to do it twice, we don't know why. You can disable it from **Settings > Change PC settings > General > Advanced startup > restart now**. After reboot you will need to go **Troubleshoot > Advanced options > Startup settings** and with key **F7** select the option **Disable driver signature enforcement**.

2. Moreover, we had to update the **android_winusb.inf file** at *myWindowsUser\sdk\extras\google\usb_driver*, with the following:

		[Google.NTamd64]

		;GoogleGlass
		%SingleAdbInterface%        = USB_Install, USB\VID_18D1&PID_4E11&REV_0216
		%CompositeAdbInterface%     = USB_Install, USB\VID_18D1&PID_4E11&MI_01
		%SingleAdbInterface%        = USB_Install, USB\VID_18D1&PID_9001&REV_0216
		%CompositeAdbInterface%     = USB_Install, USB\VID_18D1&PID_9001&MI_01

		[Google.NTx86]

		;GoogleGlass
		%SingleAdbInterface%        = USB_Install, USB\VID_18D1&PID_4E11&REV_0216
		%CompositeAdbInterface%     = USB_Install, USB\VID_18D1&PID_4E11&MI_01
		%SingleAdbInterface%        = USB_Install, USB\VID_18D1&PID_9001&REV_0216
		%CompositeAdbInterface%     = USB_Install, USB\VID_18D1&PID_9001&MI_01

3. Repeat step 1.

4. Go into your Windows Device Manager, right click on the Glass Device, select Update Driver Software. When prompted, choose "Select From Computer" and enter your **android_winusb.inf** parent folder location.

5. Open Windows Device Manager, expand Android Device, right click on "Android Composite ADB Interface" and choose "Uninstall" (don't delete driver folder). Disconnect Glass and then reconnect. After this the device will be reinstalled successfully. Android Composite ADB Interface should reappear in Device Manager.

Got it? Reopen Eclipse then! and check for the Device on the DDMS perspective!

### 3) Running Google examples

Once the device was correctly configured and recognized by Eclipse we focus to get **Compass**, **Stopwatch** and **Timer** running. Unfortunately, that wasn't an easy task too.

#### XE11 on device - GDK with XE12

> Could not find method com.google.android.glass.timeline.TimelineManager.createLiveCard, referenced from...

says the Logcat.

Deep into the blackest options in the device we finally found that the device was running XE11, an older version of the platform. The downloaded GDK was for XE12. And wireless was off because some troubles recognizing the wifi QR, so the device wasn't able to update to new version.

`createLiveCard` is a valid method on XE12 but not in XE11 ([more info](https://developers.google.com/glass/release-notes#xe12)). Update the device to solve this problem ([help](https://support.google.com/glass/answer/3226482?hl=en)).