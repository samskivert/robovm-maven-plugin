/*
 * Copyright (C) 2015 BugVM AB.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.bugvm.maven.plugin;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Parameter;
import com.bugvm.compiler.AppCompiler;
import com.bugvm.compiler.config.Arch;
import com.bugvm.compiler.config.Config;

/**
 * Abstract mojo which either archives or installs.
 */
public abstract class AbstractBugVMBuildMojo extends AbstractBugVMMojo {

    /**
     * Colon separated list of architectures to include in the archive.
     */
    @Parameter(property="bugvm.archs")
    protected String archs;

    // /**
    //  * If set to {@code true} bitcode will be generated in the linked binary.
    //  * The default is {@code false}.
    //  */
    // @Parameter(property="bugvm.enableBitcode")
    // protected boolean enableBitcode = false;

    protected String getArchs() {
        return archs;
    }

    protected abstract boolean shouldArchive();

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {

        try {

            Config.Builder builder = configure(new Config.Builder())
                    .skipInstall(false);

            if (getArchs() != null) {
                List<Arch> archs = new ArrayList<>();
                for (String s : getArchs().trim().split(":")) {
                    archs.add(Arch.valueOf(s));
                }
                builder.archs(archs);
            }
            // builder.enableBitcode(enableBitcode);

            AppCompiler compiler = new AppCompiler(builder.build());
            compiler.build();
            if (shouldArchive()) {
                compiler.archive();
            } else {
                compiler.install();
            }

        } catch (IOException e) {
            if (shouldArchive()) {
                throw new MojoExecutionException("Failed to create archive", e);
            } else {
                throw new MojoExecutionException("Failed to install", e);
            }
        }
    }
}
