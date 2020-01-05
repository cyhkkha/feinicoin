package name.feinimouse.feinicoinplus.simple.impl;

import name.feinimouse.feinicoinplus.core.data.Carrier;
import name.feinimouse.feinicoinplus.core.node.Center;
import name.feinimouse.feinicoinplus.exception.BadCommitException;

import java.security.PrivateKey;

@SuppressWarnings("RedundantThrows")
public class SimpleCenter extends Center {
    public SimpleCenter(PrivateKey privateKey) {
        super(privateKey);
    }

    @Override
    protected void beforeCommit(Carrier carrier) throws BadCommitException {

    }

    @Override
    protected void beforeFetch(Carrier carrier) throws BadCommitException {

    }

    @Override
    protected void resolveCommit(Carrier carrier) throws BadCommitException {

    }

    @Override
    protected Carrier resolveFetch(Carrier carrier) throws BadCommitException {
        return null;
    }
}
