package name.feinimouse.feinicoinplus.core;

import name.feinimouse.feinicoinplus.core.node.Node;

public interface NodeConfig {

    Node order();

    Node verifier();

    Node center();
}
