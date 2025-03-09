package com.cokebook.tools.tikoy.mapping;

import com.cokebook.tools.tikoy.mapping.annotation.JobMapping;
import lombok.Getter;

import java.util.*;

public class Matcher {

    protected Matcher(Collection<String> databases,
                      Collection<String> tables,
                      Collection<Op> ops,
                      String group
    ) {
        this(new HashSet<>(databases), new HashSet<>(tables), new HashSet<>(ops), group);
    }


    protected Matcher(Set<String> databases, Set<String> tables, Set<Op> ops, String group) {
        this.databases = databases;
        this.tables = tables;
        this.ops = ops;
        this.group = group;
    }

    @Getter
    private final String group;
    private final Set<String> databases;
    private final Set<String> tables;
    private final Set<Op> ops;


    public boolean match(String database, String table, Op op, String group) {
        return (Objects.equals(this.group, JobMapping.ALL) || Objects.equals(this.group, group)) &&
                (this.databases.contains(JobMapping.ALL) || this.databases.contains(database))
                && (this.tables.contains(JobMapping.ALL) || this.tables.contains(table))
                && (this.ops.contains(op));
    }

    public Set<String> getDatabases() {
        return Collections.unmodifiableSet(databases);
    }

    public Set<String> getTables() {
        return Collections.unmodifiableSet(tables);
    }

    public Set<Op> getOps() {
        return Collections.unmodifiableSet(ops);
    }
}
