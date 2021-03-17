package com.katyshevtseva.kikiorg.view.controller.wardrobe;

import com.katyshevtseva.fx.WindowBuilder.FxController;
import com.katyshevtseva.kikiorg.core.sections.wardrobe.entity.Outfit;

class OutfitDialogController implements FxController {
    private Outfit outfit;

    OutfitDialogController(Outfit outfit) {
        this.outfit = outfit;
    }
}
