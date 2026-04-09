package com.example.adaprivelearningnavigator.service.dto.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegisterRequest(
        @Email(message = "РќРµРєРѕСЂСЂРµРєС‚РЅС‹Р№ email")
        @NotBlank(message = "Email РѕР±СЏР·Р°С‚РµР»РµРЅ")
        String email,

        @NotBlank(message = "РџР°СЂРѕР»СЊ РѕР±СЏР·Р°С‚РµР»РµРЅ")
        @Size(min = 8, max = 100, message = "РџР°СЂРѕР»СЊ РґРѕР»Р¶РµРЅ СЃРѕРґРµСЂР¶Р°С‚СЊ РѕС‚ 8 РґРѕ 100 СЃРёРјРІРѕР»РѕРІ")
        String password,

        @NotBlank(message = "РРјСЏ РѕС‚РѕР±СЂР°Р¶РµРЅРёСЏ РѕР±СЏР·Р°С‚РµР»СЊРЅРѕ")
        @Size(max = 120, message = "РРјСЏ РѕС‚РѕР±СЂР°Р¶РµРЅРёСЏ РЅРµ РґРѕР»Р¶РЅРѕ РїСЂРµРІС‹С€Р°С‚СЊ 120 СЃРёРјРІРѕР»РѕРІ")
        String displayName
) {
}
