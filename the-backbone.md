---
layout: page
title: The backbone
---

This glassware relies on 3 main classes or parts: a Service, a Drawer and a View.

### HeadlineService

Where the glassware starts. Handles the `LiveCard` creation & destruction. When `onStartCommand` is executed, attaches the drawer callback.

### HeadlineDrawer

Implements a `SurfaceHolder.Callback`. I would say that does most of the work. Takes care of the communication with the view using a listener. When the view is updated, the HeadlineDrawer immediatly redraw the canvas.

Internally contains a `RenderThread`¹ that pools in background every few seconds and executes the `updateText` method in order to update the headline with a fresh breaking new.

### HeadlineView

Receives instructions to update the view with a breaking new and pushes a notification to the `HeadlineDrawer`. It's the connection with the headline layout. Sets the values for  the components in the view.

---

¹: I don't know if is the right place to put the thread. For the prototype serves the purpose but if I continue this, I would deepen about this.