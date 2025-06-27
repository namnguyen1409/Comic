import os

def count_files_in_directory(path):
    total_files = 0
    for root, dirs, files in os.walk(path):
        total_files += len(files)
    return total_files

# Example usage
folder_path = "/home/namnguyen/Comic/back-end/auth-service/src/main/java/top/telecomic/authservice"
print(f"Total files: {count_files_in_directory(folder_path)}")