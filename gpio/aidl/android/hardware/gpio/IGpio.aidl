/* wasn't kidding, this WAS the package name: */
package android.hardware.gpio;

/* we'll explain this Vintf stuff later... */
@VintfStability
interface IGpio {
  void setGpio(in int portn, in int state);
  int getGpio(in int portn);
}
