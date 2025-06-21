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
            ## native versions
            glfw3-minecraft
            openal

            ## openal
            libjack2
            pipewire

            ## glfw
            libGL

            flite
          ];
        });
  in {
    devShells = forEachSupportedSystem ({
      pkgs,
      libs,
    }: {
      default = pkgs.mkShell {
        packages = with pkgs; [zulu21];
        buildInputs = libs;
        LD_LIBRARY_PATH = pkgs.lib.makeLibraryPath libs;
      };
    });
  };
}
