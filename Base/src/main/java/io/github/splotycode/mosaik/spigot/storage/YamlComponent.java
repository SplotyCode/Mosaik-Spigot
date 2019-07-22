package io.github.splotycode.mosaik.spigot.storage;

public interface YamlComponent {

    void read(YamlProvider yaml);
    void save(YamlProvider yaml);

}
