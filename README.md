# MDEdit

A simple editor for the [Markdown language][1] by John Gruber to run on Android.  
Forked from [pilhuhn/MDEdit][2] and ViewPager support added to give it greater flexibility.  
Supports both phones and tablets from Android 2.3 onwards.

## Compatibility
Built with target SDK version of 15, and minimum SDK version of 9.  
This allows running the application on Android 2.3+  
Markdown Papers uses ArrayDeque, limiting its use to at least Android 2.3 (first SDK version to have ArrayDeque). If this issue is fixed, this application can potentially run on 1.6 at least.

## Dependencies
### Markdown Papers
Uses the [markdownpapers][3] library for markdown parsing and HTML translation.  
Using core version 1.2.3

### android-filechooser
Uses [android-filechooser][4] library for opening and saving files via a separate dialog.  
Using version 4.2

### ViewPagerIndicator
Uses [ViewPagerIndicator][5] library for indicating which viewpager page is currently visible.  
Using version 2.3.1

### Support Library
[Android Support Library][6] is included to allow backwards-compatibility using ViewPager.  
Using version 4, revision 9

# TODOs

- Add settings activity with colour choices for EditText
- Add ability to load other CSS
- Changeable font size for editor, along with different colour schemes.
- Remember where the currently open file was from for simpler saving
- New icon for app (now that it's becoming more complete)


[1]: http://daringfireball.net/projects/markdown/syntax
[2]: https://github.com/pilhuhn/MDEdit
[3]: http://markdown.tautua.org/
[4]: http://code.google.com/p/android-filechooser/
[5]: http://viewpagerindicator.com/
[6]: http://developer.android.com/tools/extras/support-library.html
