{ pkgs, ... }: {
  packages = [ pkgs.git ];

  languages = {
    java = {
      enable = true;
      jdk.package = pkgs.jdk17;
      maven.enable = true;
    };
    shell.enable = true;
    nix.enable = true;
  };

  services.postgres = {
    enable = true;
    package = pkgs.postgresql;
    initialDatabases = [{ name = "verma_db"; }];
  };

  devcontainer.enable = true;
  difftastic.enable = true;
  dotenv.enable = true;

  pre-commit.hooks = {
    actionlint.enable = true;
    commitizen.enable = true;
    markdownlint.enable = true;
  };
}
