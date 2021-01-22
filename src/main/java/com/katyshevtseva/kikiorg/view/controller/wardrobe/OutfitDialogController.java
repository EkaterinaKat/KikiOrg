package com.katyshevtseva.kikiorg.view.controller.wardrobe;

import com.katyshevtseva.kikiorg.core.sections.wardrobe.entity.Outfit;
import com.katyshevtseva.kikiorg.view.utils.WindowBuilder.FxController;

class OutfitDialogController implements FxController {
    private Outfit outfit;

    OutfitDialogController(Outfit outfit) {
        this.outfit = outfit;
    }
}
