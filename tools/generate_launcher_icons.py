#!/usr/bin/env python3
"""Generate Android launcher mipmaps from design/icons/poc/c-card-fan.jpg."""
from pathlib import Path

from PIL import Image

ROOT = Path(__file__).resolve().parents[1]
SRC = ROOT / "design" / "icons" / "poc" / "c-card-fan.jpg"
RES = ROOT / "apps" / "android" / "src" / "main" / "res"
DESIGN = ROOT / "design" / "icons"


def main() -> None:
    src = Image.open(SRC).convert("RGBA")
    DESIGN.mkdir(parents=True, exist_ok=True)
    master = src.resize((1024, 1024), Image.Resampling.LANCZOS)
    master.save(DESIGN / "app-icon-source.png", optimize=True)

    def make_fg(size: int) -> Image.Image:
        canvas = Image.new("RGBA", (size, size), (0, 0, 0, 0))
        inner = int(size * 0.72)
        icon = master.resize((inner, inner), Image.Resampling.LANCZOS)
        offset = (size - inner) // 2
        canvas.paste(icon, (offset, offset), icon)
        return canvas

    def make_legacy(size: int) -> Image.Image:
        return master.resize((size, size), Image.Resampling.LANCZOS)

    densities = {
        "mipmap-mdpi": 48,
        "mipmap-hdpi": 72,
        "mipmap-xhdpi": 96,
        "mipmap-xxhdpi": 144,
        "mipmap-xxxhdpi": 192,
    }
    fg_densities = {
        "mipmap-mdpi": 108,
        "mipmap-hdpi": 162,
        "mipmap-xhdpi": 216,
        "mipmap-xxhdpi": 324,
        "mipmap-xxxhdpi": 432,
    }

    for folder, size in densities.items():
        d = RES / folder
        d.mkdir(parents=True, exist_ok=True)
        make_legacy(size).save(d / "ic_launcher.png", optimize=True)
        make_legacy(size).save(d / "ic_launcher_round.png", optimize=True)

    for folder, size in fg_densities.items():
        d = RES / folder
        d.mkdir(parents=True, exist_ok=True)
        make_fg(size).save(d / "ic_launcher_foreground.png", optimize=True)

    values = RES / "values"
    values.mkdir(parents=True, exist_ok=True)
    (values / "colors.xml").write_text(
        '<?xml version="1.0" encoding="utf-8"?>\n'
        "<resources>\n"
        '    <color name="ic_launcher_background">#020617</color>\n'
        "</resources>\n",
        encoding="utf-8",
    )

    anydpi = RES / "mipmap-anydpi-v26"
    anydpi.mkdir(parents=True, exist_ok=True)
    adaptive = (
        '<?xml version="1.0" encoding="utf-8"?>\n'
        '<adaptive-icon xmlns:android="http://schemas.android.com/apk/res/android">\n'
        '    <background android:drawable="@color/ic_launcher_background"/>\n'
        '    <foreground android:drawable="@mipmap/ic_launcher_foreground"/>\n'
        "</adaptive-icon>\n"
    )
    (anydpi / "ic_launcher.xml").write_text(adaptive, encoding="utf-8")
    (anydpi / "ic_launcher_round.xml").write_text(adaptive, encoding="utf-8")
    print(f"Wrote launcher assets under {RES}")


if __name__ == "__main__":
    main()
