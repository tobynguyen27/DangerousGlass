{
  inputs.nixpkgs.url = "github:NixOS/nixpkgs/nixos-unstable";

  outputs = inputs: let
    # openal doesn't support Darwin, I may add it back if I know an alternative package
    supportedSystems = ["x86_64-linux" "aarch64-linux"];
    forEachSupportedSystem = f:
      inputs.nixpkgs.lib.genAttrs supportedSystems (system:
        f rec {
          pkgs = import inputs.nixpkgs {inherit system;};

          libs = with pkgs; [
            glfw3-minecraft
            openal

            ## openal
            alsa-lib
            libjack2
            libpulseaudio
            pipewire

            ## glfw
            libGL
            xorg.libX11
            xorg.libXcursor
            xorg.libXext
            xorg.libXrandr
            xorg.libXxf86vm

            udev # oshi

            vulkan-loader # VulkanMod's lwjgl
            flite # text2speech
          ];
        });
  in {
    devShells = forEachSupportedSystem ({
      pkgs,
      libs,
    }: {
      default = pkgs.mkShell {
        packages = with pkgs; [mesa-demos pciutils xorg.xrandr zulu21];
        buildInputs = libs;
        LD_LIBRARY_PATH = pkgs.lib.makeLibraryPath libs;
      };
    });
  };
}