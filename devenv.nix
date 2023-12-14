{ pkgs, ... }: {
  packages = [ pkgs.git ];

  languages = {
    java = {
        enable = true;
        jdk.package = pkgs.jdk17;
        maven.enable = true;
    };
  };

  services.postgres = {
    enable = true;
    package = pkgs.postgresql;
    initialDatabases = [{ name = "dera_db"; }];
  };

  devcontainer.enable = true;
  difftastic.enable = true;
}
