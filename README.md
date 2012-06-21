# MDEdit

A simple editor for the [Markdown language][2] by John Gruber to run on Android.  
Forked from [pilhuhn/MDEdit][3] and ViewPager support added to give it greater flexibility.  
Supports both phones and tablets from Android 2.3 onwards (something to do with scrollbars is bugging, causing app to crash on 1.6, and markdownpapers won't work on anything under Android 2.3 due to using ArrayDeque).

## Dependencies
This uses the [markdownpapers][1] library for markdown parsing and HTML translation.  
Current version is 1.2.3

[1]: http://markdown.tautua.org/
[2]: http://daringfireball.net/projects/markdown/syntax
[3]: https://github.com/pilhuhn/MDEdit