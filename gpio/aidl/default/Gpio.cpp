#define LOG_TAG "Gpio"

#include <android-base/logging.h>
#include "Gpio.h"
#include <gpiod.h>

namespace aidl::android::hardware::gpio {

::ndk::ScopedAStatus Gpio::setGpio(int32_t in_portn, int32_t in_state)
{
        const char *chipname = "gpiochip0";
    struct gpiod_chip *chip = gpiod_chip_open_by_name(chipname);
    if (!chip) {
        LOG(ERROR) << "Failed to open " << chipname;
        return ::ndk::ScopedAStatus::fromExceptionCode(EX_ILLEGAL_STATE);
    }

    struct gpiod_line *line = gpiod_chip_get_line(chip, in_portn);
    if (!line) {
        LOG(ERROR) << "Failed to get line " << in_portn;
        gpiod_chip_close(chip);
        return ::ndk::ScopedAStatus::fromExceptionCode(EX_ILLEGAL_ARGUMENT);
    }

    int ret = gpiod_line_request_output(line, "gpio-service", in_state);
    if (ret < 0) {
        LOG(ERROR) << "Failed to request line as output: GPIO " << in_portn;
        gpiod_chip_close(chip);
        return ::ndk::ScopedAStatus::fromExceptionCode(EX_ILLEGAL_STATE);
    }

    ret = gpiod_line_set_value(line, in_state);
    if (ret < 0) {
        LOG(ERROR) << "Failed to set GPIO value";
        gpiod_chip_close(chip);
        return ::ndk::ScopedAStatus::fromExceptionCode(EX_ILLEGAL_STATE);
    }

    gpiod_chip_close(chip);
    LOG(INFO) << "GPIO " << in_portn << " set to " << in_state;
    return ::ndk::ScopedAStatus::ok();
}

::ndk::ScopedAStatus Gpio::getGpio(int32_t in_portn, int32_t* _aidl_return)
{
        (void)(in_portn);
        *_aidl_return = 0;
        LOG(INFO) << "yay2";
  return ::ndk::ScopedAStatus::ok();
}

}
