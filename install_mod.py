import os
import sys
import platform
import subprocess
import urllib.request
import shutil

# Configuration
FORGE_VERSION = "1.21.4-49.0.35"
FORGE_URL = f"https://maven.minecraftforge.net/net/minecraftforge/forge/{FORGE_VERSION}/forge-{FORGE_VERSION}-installer.jar"
MOD_FILE = "ai_villagers-1.0.jar"

def get_minecraft_dir():
    system = platform.system()
    if system == "Windows":
        return os.path.join(os.getenv('APPDATA'), '.minecraft')
    elif system == "Darwin":  # Mac
        return os.path.expanduser('~/Library/Application Support/minecraft')
    else:  # Linux/Unix
        return os.path.expanduser('~/.minecraft')

def install_forge(mc_dir):
    print("Downloading Forge installer...")
    installer_path = os.path.join(mc_dir, 'forge-installer.jar')
    urllib.request.urlretrieve(FORGE_URL, installer_path)
    
    print("Installing Forge...")
    subprocess.run(["java", "-jar", installer_path, "--installServer"], check=True)
    os.remove(installer_path)
    print("Forge installed successfully")

def install_mod(mc_dir):
    mods_dir = os.path.join(mc_dir, 'mods')
    os.makedirs(mods_dir, exist_ok=True)
    
    mod_src = os.path.join(os.getcwd(), 'build', 'libs', MOD_FILE)
    if not os.path.exists(mod_src):
        print("Error: Mod file not found. Please build the mod first.")
        return False
        
    shutil.copy(mod_src, mods_dir)
    print(f"Mod installed to {mods_dir}")
    return True

def main():
    try:
        mc_dir = get_minecraft_dir()
        if not os.path.exists(mc_dir):
            print("Minecraft directory not found. Is Minecraft installed?")
            return
            
        install_forge(mc_dir)
        if not install_mod(mc_dir):
            return
            
        print("Installation completed successfully!")
    except Exception as e:
        print(f"Error during installation: {str(e)}")
        sys.exit(1)

if __name__ == "__main__":
    main()