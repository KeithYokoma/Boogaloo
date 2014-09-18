# Boogaloo

Task retry manager with a certain back off time.

![](https://farm8.staticflickr.com/7231/7285682482_ebc0f98a8c_k_d.jpg)
[Photo License](https://creativecommons.org/licenses/by-sa/2.0/)

# Usage

```java
// the task is executed per second and after 10 seconds passed the task is no longer retried to execute.
Boogaloo.setup().constant().interval(1000).until(10000).execute(new BackoffTask() {
  @Override
  protected boolean shouldRetry() {
    return getCount() <= 5; // this back off task tries 5 times.
  }

  @Override
  public void run() {
    // some task on the main thread.
  }
});
```

# License

Apache License v2

```
Copyright (C) 2014 KeithYokoma, Inc. All rights reserved.

Licensed under the Apache License, Version 2.0 (the "License"); you may not use
this file except in compliance with the License. You may obtain a copy of the
License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software distributed
under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR
CONDITIONS OF ANY KIND, either express or implied. See the License for the
specific language governing permissions and limitations under the License.
```
