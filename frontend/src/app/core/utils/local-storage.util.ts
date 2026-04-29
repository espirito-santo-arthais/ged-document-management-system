export class LocalStorageUtil {

    static set<T>(key: string, value: T): void {
        localStorage.setItem(key, JSON.stringify(value));
    }

    static get<T>(key: string): T | null {
        const data = localStorage.getItem(key);
        return data ? JSON.parse(data) : null;
    }

    static remove(key: string): void {
        localStorage.removeItem(key);
    }
}